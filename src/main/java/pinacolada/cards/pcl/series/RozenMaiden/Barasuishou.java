package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Barasuishou extends PCLCard
{
    public static final PCLCardData DATA = Register(Barasuishou.class)
    		.SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Dark, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    public Barasuishou()
    {
        super(DATA);

        Initialize(8, 0, 2, 1);
        SetUpgrade(3, 0, 1);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Dark(1, 0, 2);

        SetEthereal(true);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (magicNumber *
                (PCLJUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.hand.group, c -> c.type == CardType.CURSE)));
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.DARK);
    }


    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this)) {
            PCLActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), secondaryValue);
            PCLActions.Last.Exhaust(this);
        }
    }
}


