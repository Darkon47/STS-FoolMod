package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ImperialArchers;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MoltSolAugustus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    private static final int ENERGY_COST = 1;
    static
    {
        DATA.AddPreview(new ImperialArchers(), false);
    }

    public MoltSolAugustus()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new MoltSolAugustusPower(p, secondaryValue));
    }

    public static class MoltSolAugustusPower extends AnimatorClickablePower
    {
        public MoltSolAugustusPower(AbstractCreature owner, int amount)
        {
            super(owner, MoltSolAugustus.DATA, PowerTriggerConditionType.Energy, MoltSolAugustus.ENERGY_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.SelectFromPile(name, Integer.MAX_VALUE, player.drawPile, player.discardPile, player.hand)
            .SetOptions(false, false)
            .SetFilter(c -> GameUtilities.GetAffinityLevel(c, Affinity.Red, true) > 0)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    ((EYBCard)c).SetHaste(true);
                }
            });

            flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.MakeCardInDrawPile(new ImperialArchers())
            .Repeat(amount).SetDuration(0.1f, false);
        }
    }
}