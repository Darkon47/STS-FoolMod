package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCustomCardSlot;
import pinacolada.resources.PGR;
import pinacolada.ui.AbstractScreen;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_CardGrid;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.controls.GUI_Toggle;
import pinacolada.ui.hitboxes.AdvancedHitbox;

import java.util.HashMap;

public class PCLCustomCardsScreen extends AbstractScreen {

    protected static final float ITEM_HEIGHT = AbstractCard.IMG_HEIGHT * 0.15f;
    private static final float DRAW_START_X = (Settings.WIDTH - (5f * AbstractCard.IMG_WIDTH * 0.75f) - (4f * Settings.CARD_VIEW_PAD_X) + AbstractCard.IMG_WIDTH * 0.75f);
    private static final float DRAW_START_Y = (float) Settings.HEIGHT * 0.7f;
    private static final float PAD_Y = AbstractCard.IMG_HEIGHT * 0.75f + Settings.CARD_VIEW_PAD_Y;
    private static final float SCROLL_BAR_THRESHOLD = 500f * Settings.scale;

    protected GUI_Image background_image;
    protected ActionT0 OnClose;
    private final GUI_Toggle Toggle;
    private final GUI_CardGrid Grid;
    protected AbstractCard.CardColor CurrentColor;
    protected GUI_Button add_button;
    protected GUI_Button cancel_button;
    protected PCLCustomCardEditEffect editEffect;
    protected HashMap<PCLCard, PCLCustomCardSlot> CurrentSlots = new HashMap<>();

    public PCLCustomCardsScreen() {
        final float buttonHeight = ScreenH(0.07f);
        final float labelHeight = ScreenH(0.04f);
        final float buttonWidth = ScreenW(0.18f);
        final float labelWidth = ScreenW(0.20f);
        final float button_cY = buttonHeight * 1.5f;

        background_image = new GUI_Image(PGR.PCL.Images.FullSquare.Texture(), new Hitbox(ScreenW(1), ScreenH(1)))
                .SetPosition(ScreenW(0.5f), ScreenH(0.5f))
                .SetColor(0, 0, 0, 0.9f);

        this.Grid = new GUI_CardGrid(1f)
                .AddPadX(AbstractCard.IMG_WIDTH * 0.5f)
                .AddPadY(AbstractCard.IMG_HEIGHT * 0.15f)
                .SetEnlargeOnHover(false)
                .SetOnCardClick(c -> OnCardClicked((PCLCard) c))
                .SetOnCardRender((sb, c) -> OnCardRender(sb, (PCLCard) c));
        Toggle = new GUI_Toggle(new AdvancedHitbox(0, 0, AbstractCard.IMG_WIDTH * 0.2f, ITEM_HEIGHT))
                .SetBackground(PGR.PCL.Images.Panel.Texture(), com.badlogic.gdx.graphics.Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.65f)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f);
        cancel_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .SetPosition(buttonWidth * 0.6f, button_cY)
                .SetColor(Color.FIREBRICK)
                .SetText(GridCardSelectScreen.TEXT[1])
                .SetOnClick(AbstractDungeon::closeCurrentScreen);

        add_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .SetPosition(cancel_button.hb.cX, cancel_button.hb.y + cancel_button.hb.height + labelHeight * 0.8f)
                .SetColor(Color.WHITE)
                .SetText(PGR.PCL.Strings.CardEditor.NewCard)
                .SetOnClick(this::Add);
    }

    public void Open(CharacterOption option, ActionT0 onClose)
    {
        super.Open();

        CurrentColor = option.c.getCardColor();
        CurrentSlots.clear();
        Grid.Clear();
        for (PCLCustomCardSlot slot : PCLCustomCardSlot.GetCards(CurrentColor)) {
            PCLCard card = slot.Builder.Build();
            CurrentSlots.put(card, slot);
            Grid.AddCard(card);
        };
    }

    @Override
    public void Update()
    {
        background_image.Update();
        if (editEffect != null)
        {
            editEffect.update();

            if (editEffect.isDone)
            {
                editEffect = null;
            }
        }
        else {
            Grid.TryUpdate();
            cancel_button.TryUpdate();
            add_button.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_image.Render(sb);
        if (editEffect != null) {
            editEffect.render(sb);
        }
        else {
            Grid.TryRender(sb);
            cancel_button.TryRender(sb);
            add_button.TryRender(sb);
        }
    }

    public void Add()
    {
        if (editEffect == null) {
            PCLCustomCardSlot slot = new PCLCustomCardSlot(CurrentColor);
            editEffect = new PCLCustomCardEditEffect(slot)
                    .SetActions(() -> {}, () -> {
                        PCLCard newCard = slot.Builder.Build();
                        CurrentSlots.put(newCard, slot);
                        PCLCustomCardSlot.GetCards(CurrentColor).add(slot);
                        Grid.AddCard(newCard);
                        slot.CommitBuilder();
                    });
        }
    }

    public void Edit(PCLCard card, PCLCustomCardSlot cardSlot)
    {
        if (editEffect == null && cardSlot != null) {
            editEffect = new PCLCustomCardEditEffect(cardSlot)
                    .SetActions(() -> Remove(card, cardSlot),
                            () -> {
                        PCLCard newCard = cardSlot.Builder.Build();
                                Grid.RemoveCard(card);
                                CurrentSlots.remove(card);
                                CurrentSlots.put(newCard, cardSlot);
                                Grid.AddCard(newCard);
                                cardSlot.CommitBuilder();
                    });;
        }
    }

    public void Remove(PCLCard card, PCLCustomCardSlot cardSlot)
    {
        if (editEffect == null) {
            Grid.RemoveCard(card);
            CurrentSlots.remove(card);
            cardSlot.WipeBuilder();
        }
    }

    private void OnCardClicked(PCLCard card)
    {
        PCLCustomCardSlot slot = CurrentSlots.get(card);
        if (slot != null) {
            Edit(card, slot);
        }
    }

    private void OnCardRender(SpriteBatch sb, PCLCard card)
    {
        PCLCustomCardSlot slot = CurrentSlots.get(card);
        if (slot != null) {
            Toggle.SetOnToggle(val -> slot.IsEnabled = val)
                    .SetToggle(slot.IsEnabled)
                    .SetPosition(card.hb.cX, card.hb.cY - (card.hb.height * 0.65f))
                    .Render(sb);
        }

    }
}
