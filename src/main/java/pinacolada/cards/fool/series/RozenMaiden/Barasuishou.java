package pinacolada.cards.fool.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Barasuishou extends FoolCard
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

        SetFragile(true);
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
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), secondaryValue);
    }
}


