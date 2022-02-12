package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Dominate extends EternalCard
{
    public static final PCLCardData DATA = Register(Dominate.class).SetAttack(2, CardRarity.COMMON);

    public Dominate()
    {
        super(DATA);

        Initialize(5, 0, 2);
        SetUpgrade(3, 0);

        SetDark();
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        amount = amount + PCLCombatStats.MatchingSystem.ResolveMeter.Resolve() * magicNumber;
        if (CheckPrimaryCondition(true)) {
            amount *= 1.2f;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }
}