package pinacolada.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.ListSelection;
import pinacolada.resources.GR;
import pinacolada.ui.GridCardSelectScreenHelper;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFromPile extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected final CardGroup fakeHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final CardGroup[] groups;

    protected GenericCondition<ArrayList<AbstractCard>> condition;
    protected GenericCondition<AbstractCard> filter;
    protected FuncT1<String, ArrayList<AbstractCard>> dynamicString;
    protected ListSelection<AbstractCard> origin;
    protected ListSelection<AbstractCard> maxChoicesOrigin;
    protected int maxChoices;
    protected boolean hideTopPanel;
    protected boolean canPlayerCancel;
    protected boolean anyNumber;
    protected boolean selected;
    protected boolean forTransform;
    protected boolean forUpgrade;
    protected boolean forPurge;

    public SelectFromPile(String sourceName, int amount, CardGroup... groups)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, groups);
    }

    public SelectFromPile(ActionType type, String sourceName, int amount, CardGroup... groups)
    {
        super(type);

        this.groups = groups;
        this.canPlayerCancel = false;
        this.message = GR.PCL.Strings.GridSelection.ChooseCards_F1;

        Initialize(amount, sourceName);
    }

    public SelectFromPile HideTopPanel(boolean hideTopPanel)
    {
        this.hideTopPanel = hideTopPanel;

        return this;
    }

    public SelectFromPile CancellableFromPlayer(boolean value)
    {
        this.canPlayerCancel = value;

        return this;
    }

    public SelectFromPile SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public SelectFromPile SetMessage(String format, Object... args)
    {
        this.message = PCLJUtils.Format(format, args);

        return this;
    }

    public SelectFromPile SetDynamicMessage(FuncT1<String, ArrayList<AbstractCard>> stringFunc) {
        this.dynamicString = stringFunc;

        return this;
    }

    public SelectFromPile SetOptions(boolean isRandom, boolean anyNumber)
    {
        return SetOptions(isRandom ? CardSelection.Random : null, anyNumber);
    }

    public SelectFromPile SetOptions(ListSelection<AbstractCard> origin, boolean anyNumber)
    {
        return SetOptions(origin, anyNumber, false, false, false);
    }

    public SelectFromPile SetOptions(ListSelection<AbstractCard> origin, boolean anyNumber, boolean forTransform, boolean forUpgrade, boolean forPurge)
    {
        this.anyNumber = anyNumber;
        this.origin = origin;
        this.forTransform = forTransform;
        this.forUpgrade = forUpgrade;
        this.forPurge = forPurge;

        return this;
    }

    public SelectFromPile SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> SelectFromPile SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    public SelectFromPile SetMaxChoices(Integer maxChoices)
    {
        return SetMaxChoices(maxChoices, CardSelection.Random);
    }

    public SelectFromPile SetMaxChoices(Integer maxChoices, ListSelection<AbstractCard> origin)
    {
        this.maxChoices = maxChoices;
        this.maxChoicesOrigin = origin;

        return this;
    }

    public SelectFromPile SetCompletionRequirement(FuncT1<Boolean, ArrayList<AbstractCard>> condition)
    {
        this.condition = GenericCondition.FromT1(condition);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (hideTopPanel)
        {
            PCLGameUtilities.SetTopPanelVisible(false);
        }

        GridCardSelectScreenHelper.Clear(true);
        GridCardSelectScreenHelper.SetCondition(condition);
        GridCardSelectScreenHelper.SetDynamicLabel(dynamicString);

        for (CardGroup group : groups)
        {
            CardGroup temp = new CardGroup(group.type);
            for (AbstractCard card : group.group)
            {
                if (filter == null || filter.Check(card))
                {
                    AddCard(temp, card);
                }
            }

            if (temp.type == CardGroup.CardGroupType.HAND && origin == null)
            {
                fakeHandGroup.group.addAll(temp.group);

                for (AbstractCard c : temp.group)
                {
                    player.hand.removeCard(c);
                    c.stopGlowing();
                }

                GridCardSelectScreenHelper.AddGroup(fakeHandGroup);
            }
            else
            {
                if (temp.type == CardGroup.CardGroupType.DRAW_PILE && origin == null)
                {
                    if (PCLGameUtilities.HasRelicEffect(FrozenEye.ID))
                    {
                        Collections.reverse(temp.group);
                    }
                    else
                    {
                        temp.sortAlphabetically(true);
                        temp.sortByRarityPlusStatusCardType(true);
                    }
                }

                GridCardSelectScreenHelper.AddGroup(temp);
            }
        }

        CardGroup mergedGroup = GridCardSelectScreenHelper.GetCardGroup();
        if (mergedGroup.isEmpty())
        {
            player.hand.group.addAll(fakeHandGroup.group);
            GridCardSelectScreenHelper.Clear(true);
            Complete();
            return;
        }

        if (maxChoicesOrigin != null) {
            List<AbstractCard> temp = new ArrayList<>(mergedGroup.group);
            CardGroup newMerged = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            GetCardSubset(temp, newMerged.group, maxChoicesOrigin, maxChoices);
            GridCardSelectScreenHelper.Clear(false);
            GridCardSelectScreenHelper.AddGroup(newMerged);
            mergedGroup = GridCardSelectScreenHelper.GetCardGroup();
        }

        if (origin != null)
        {
            List<AbstractCard> temp = new ArrayList<>(mergedGroup.group);
            GetCardSubset(temp, selectedCards, origin, amount);
            selected = true;
            GridCardSelectScreenHelper.Clear(true);
            Complete(selectedCards);
        }
        else
        {
            if (canPlayerCancel)
            {
                // Setting canCancel to true does not ensure the cancel button will be shown...
                AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
            }
            if (anyNumber)
            {
                AbstractDungeon.gridSelectScreen.open(mergedGroup, amount, true, dynamicString == null ? UpdateMessage() : "");
            }
            else
            {
                if (!canPlayerCancel && amount > 1 && amount > mergedGroup.size())
                {
                    AbstractDungeon.gridSelectScreen.selectedCards.addAll(mergedGroup.group);
                    return;
                }

                AbstractDungeon.gridSelectScreen.open(mergedGroup, Math.min(mergedGroup.size(), amount), dynamicString == null ? UpdateMessage() : "", forUpgrade, forTransform, canPlayerCancel, forPurge);
            }
        }
    }

    protected void AddCard(CardGroup group, AbstractCard card)
    {
        group.group.add(card);

        if (!card.isSeen)
        {
            UnlockTracker.markCardAsSeen(card.cardID);
            card.isLocked = false;
            card.isSeen = true;
        }
    }

    protected void GetCardSubset(List<AbstractCard> source, List<AbstractCard> dest, ListSelection<AbstractCard> o, int count) {
        boolean remove = o.mode.IsRandom();
        int max = Math.min(source.size(), count);
        for (int i = 0; i < max; i++)
        {
            final AbstractCard card = o.Get(source, i, remove);
            if (card != null)
            {
                dest.add(card);
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
        {
            selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            selected = true;

            player.hand.group.addAll(fakeHandGroup.group);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            GridCardSelectScreenHelper.Clear(true);
        }

        if (selected)
        {
            if (TickDuration(deltaTime))
            {
                Complete(selectedCards);
            }
            return;
        }

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) // cancelled
        {
            player.hand.group.addAll(fakeHandGroup.group);
            Complete();
        }
    }

    @Override
    protected void Complete()
    {
        if (hideTopPanel)
        {
            PCLGameUtilities.SetTopPanelVisible(true);
        }

        super.Complete();
    }
}
