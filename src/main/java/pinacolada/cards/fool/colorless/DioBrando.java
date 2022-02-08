package pinacolada.cards.fool.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.DioBrando_TheWorld;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class DioBrando extends FoolCard
{
    public static final PCLCardData DATA = Register(DioBrando.class).SetAttack(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Jojo)
            .PostInitialize(data -> data.AddPreview(new DioBrando_TheWorld(), false));

    private int turns;

    public DioBrando()
    {
        super(DATA);

        Initialize(20, 0, 3, 1);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1,0,2);
        SetAffinity_Dark(1, 0, 1);

        SetSoul(6, 0, DioBrando_TheWorld::new);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        this.AddScaling(PCLAffinity.Red, 1);
        this.AddScaling(PCLAffinity.Light, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY).forEach(d -> d.SetVFXColor(Color.GOLDENROD, Color.GOLDENROD).SetSoundPitch(0.5f, 1.5f));

        PCLActions.Bottom.Draw(magicNumber).AddCallback(() -> {
           PCLActions.Bottom.SelectFromHand(name, player.hand.size(), false)
                    .SetOptions(true,true,true)
                    .SetMessage(PGR.PCL.Strings.HandSelection.MoveToDrawPile)
                    .AddCallback(cards ->
                    {
                        for (int i = cards.size() - 1; i >= 0; i--)
                        {
                            PCLActions.Top.MoveCard(cards.get(i), player.hand, player.drawPile);
                        }
                        PCLActions.Top.GainTemporaryHP(secondaryValue * cards.size());
                    });
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}