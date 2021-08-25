package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.defect.RemoveAllOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Truth extends AnimatorCard_UltraRare
{
    private static final Crystallize status = new Crystallize();

    public static final EYBCardData DATA = Register(Truth.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FullmetalAlchemist);
    static
    {
        DATA.AddPreview(new FakeAbstractCard(status), false);
    }

    public Truth()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Light(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        int amount = 1;

        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.GainEnergy(magicNumber);

        int count = 0;
        ArrayList<String> orbs = new ArrayList<>();
        for (AbstractOrb orb : p.orbs)
        {
            if (!(orb instanceof EmptyOrbSlot) && !orbs.contains(orb.ID))
            {
                orbs.add(orb.ID);
                count += 1;
            }
        }

        if (count >= 3)
        {
            GameActions.Bottom.Add(new RemoveAllOrbsAction());
        }
        else
        {
            AddWound(p);
        }
    }

    private void AddWound(AbstractPlayer p)
    {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (!c.cardID.equals(status.cardID) && !c.uuid.equals(uuid) && GameUtilities.CanRemoveFromDeck(c))
            {
                temp.group.add(c);
            }
        }

        if (temp.size() > 0)
        {
            GameActions.Bottom.SelectFromPile(name, 1, temp)
            .SetOptions(false, false)
            .SetMessage(GR.Common.Strings.GridSelection.TransformInto(status.name))
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    player.masterDeck.removeCard(cards.get(0));
                    player.masterDeck.addToTop(status.makeCopy());
                }
            });
        }
    }
}