package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Darkness_Adrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Darkness extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Darkness.class)
            .SetSkill(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Darkness_Adrenaline(), false));

    public Darkness()
    {
        super(DATA);

        Initialize(0, 13, 3, 5);
        SetUpgrade(0, 1, 1, -1);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new DarknessPower(p, magicNumber));

        if (info.IsSynergizing) {
            GameUtilities.ModifyCostForCombat(this,-1,true);
            this.baseBlock -= secondaryValue;
        }
    }

    public class DarknessPower extends AnimatorPower
    {

        public DarknessPower(AbstractPlayer owner, int amount)
        {
            super(owner, Darkness.DATA);

            Initialize(amount);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 5)
            {
                GameActions.Bottom.GainForce(amount);
                RemovePower();

                this.flash();
            }
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            RemovePower();
        }

    }

}