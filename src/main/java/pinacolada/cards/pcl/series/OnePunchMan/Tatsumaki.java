package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PCLPower;
import pinacolada.stances.pcl.InvocationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Tatsumaki extends PCLCard
{
    public static final PCLCardData DATA = Register(Tatsumaki.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 4, 3);
        SetUpgrade(0, 1, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Light(1);
        SetAffinity_Green(0,0,1);

        SetEvokeOrbCount(1);
        SetAffinityRequirement(PCLAffinity.Red, 3);
        SetAffinityRequirement(PCLAffinity.Blue, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ChannelOrb(new Air());
        PCLActions.Bottom.StackPower(new TatsumakiPower(p, magicNumber));
        if (IsStarter() && TrySpendAffinity(PCLAffinity.Red, PCLAffinity.Blue)) {
            PCLActions.Bottom.ChangeStance(InvocationStance.STANCE_ID);
        }
    }

    public static class TatsumakiPower extends PCLPower
    {
        public TatsumakiPower(AbstractCreature owner, int amount)
        {
            super(owner, Tatsumaki.DATA);

            Initialize(amount);
        }

        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            if (type == DamageInfo.DamageType.NORMAL && card != null && PCLGameUtilities.HasLightAffinity(card) && card.baseDamage > 0) {
                damage += amount;
            }

            return super.atDamageGive(damage, type, card);
        }

        @Override
        public float modifyBlock(float blockAmount, AbstractCard card) {
            if (card != null && PCLGameUtilities.HasLightAffinity(card) && card.baseBlock > 0) {
                blockAmount += amount;
            }
            return super.modifyBlock(blockAmount, card);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            RemovePower(PCLActions.Delayed);
        }
    }
}