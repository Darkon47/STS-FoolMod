package pinacolada.cards.fool.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Mitsurugi extends FoolCard
{
    public static final PCLCardData DATA = Register(Mitsurugi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Mitsurugi()
    {
        super(DATA);

        Initialize(1, 0, 2, 7);
        SetUpgrade(0, 0, 0, 3);

        SetAffinity_Red(1, 0, 1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && PCLGameUtilities.IsAttacking(enemy.intent))
        {
            return super.ModifyDamage(enemy, amount + secondaryValue);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.AddAffinity(PCLAffinity.Red, magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        if (m == null || !PCLGameUtilities.IsAttacking(m.intent))
        {
            PCLActions.Bottom.AddAffinity(PCLAffinity.Red, magicNumber);
        }

    }
}