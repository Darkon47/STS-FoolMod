package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainOrBoost;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class FielNirvalen extends AnimatorCard
{
    public static final int SCRY_AMOUNT = 2;
    public static final EYBCardData DATA = Register(FielNirvalen.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public FielNirvalen()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, false).SetText(GetSecondaryValueString());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(secondaryValue);
        GameActions.Bottom.StackPower(new FielNirvalenPower(p, SCRY_AMOUNT));

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_GainOrBoost(GR.Tooltips.Agility, 1, true));
            choices.AddEffect(new GenericEffect_GainOrBoost(GR.Tooltips.Intellect, 1, true));
            choices.AddEffect(new GenericEffect_GainOrBoost(GR.Tooltips.Force, 1, true));
        }

        choices.Select(magicNumber, m);
    }

    public static class FielNirvalenPower extends AnimatorPower implements OnShuffleSubscriber
    {
        public FielNirvalenPower(AbstractCreature owner, int amount)
        {
            super(owner, FielNirvalen.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            CombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            enabled = true;
        }

        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            if (!owner.powers.contains(this))
            {
                CombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            if (enabled)
            {
                GameActions.Bottom.Scry(amount);
                enabled = false;
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}