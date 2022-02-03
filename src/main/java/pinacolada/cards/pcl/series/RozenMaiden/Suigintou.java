package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.Suigintou_BlackFeather;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.affinity.DesecrationPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Suigintou extends PCLCard
{
    public static final PCLCardData DATA = Register(Suigintou.class)
    		.SetAttack(2, CardRarity.RARE, PCLAttackType.Dark).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Suigintou_BlackFeather(), false));
    
    public Suigintou()
    {
        super(DATA);

        Initialize(3, 0, 2, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
        SetUnique(true, -1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        PCLActions.Top.MakeCardInHand(new Suigintou_BlackFeather());

        super.triggerOnManualDiscard();
    }

    @Override
    public void triggerOnPurge() {
        PCLActions.Top.MakeCardInHand(new Suigintou_BlackFeather());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DARKNESS);

        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Dark, secondaryValue).AddCallback(() -> {
            for (AbstractOrb orb : player.orbs) {
                if (Dark.ORB_ID.equals(orb.ID)) {
                    PCLGameUtilities.ModifyOrbBaseFocus(orb, magicNumber + PCLGameUtilities.GetPowerAmount(DesecrationPower.POWER_ID), true, false);
                }
            }
            PCLActions.Bottom.TriggerOrbPassive(PCLJUtils.Count(player.hand.group, PCLGameUtilities::IsHindrance)).SetFilter(o -> Dark.ORB_ID.equals(o.ID));
        });

    }
}
