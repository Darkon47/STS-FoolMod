package pinacolada.cards.fool.series.FullmetalAlchemist;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.FoolPower;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class Pride extends FoolCard
{
    public static final PCLCardData DATA = Register(Pride.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Pride()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);

        SetAffinity_Dark(1,0,1);

        SetEvokeOrbCount(magicNumber);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Dark, magicNumber);
        PCLActions.Bottom.StackPower(new PridePower(p, secondaryValue));
    }

    public static class PridePower extends FoolPower
    {
        public PridePower(AbstractPlayer owner, int amount)
        {
            super(owner, Pride.DATA);

            this.priority = -99;

            Initialize(amount);
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
        {
            final ArrayList<AbstractOrb> darkOrbs = new ArrayList<>();
            for (AbstractOrb orb : player.orbs)
            {
                if (Dark.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
                {
                    darkOrbs.add(orb);
                }
            }

            if (darkOrbs.size() > 0)
            {
                if (damageAmount > 0)
                {
                    damageAmount = AbsorbDamage(damageAmount, darkOrbs);
                }

                if (info.owner != null && info.owner.isPlayer != owner.isPlayer)
                {
                    PCLActions.Bottom.ApplyConstricted(TargetHelper.Normal(info.owner), amount);
                    flashWithoutSound();
                }
            }

            return super.onAttackedToChangeDamage(info, damageAmount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            RemovePower();
        }

        private int AbsorbDamage(int damage, ArrayList<AbstractOrb> darkOrbs)
        {
            final AbstractOrb next = darkOrbs.get(0);
            final float temp = damage;

            damage = Math.max(0, damage - next.evokeAmount);
            next.evokeAmount -= temp;

            if (next.evokeAmount <= 0)
            {
                darkOrbs.remove(next);
                next.evokeAmount = 0;
                PCLActions.Top.Add(new EvokeSpecificOrbAction(next));
            }

            return (damage > 0 && darkOrbs.size() > 0) ? AbsorbDamage(damage, darkOrbs) : damage;
        }
    }
}