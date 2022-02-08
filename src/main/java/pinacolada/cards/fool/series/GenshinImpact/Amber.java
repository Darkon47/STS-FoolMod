package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;

public class Amber extends FoolCard {
    public static final PCLCardData DATA = Register(Amber.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public Amber() {
        super(DATA);

        Initialize(4, 1, 2, 2);
        SetUpgrade(2, 1, 0);
        SetAffinity_Green(1, 0 ,1);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            PCLActions.Bottom.ChannelOrb(new Fire());
        }

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.ApplyLockOn(p,m,magicNumber);
        }
    }
}