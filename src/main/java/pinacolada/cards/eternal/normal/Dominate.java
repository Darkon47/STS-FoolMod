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
    public static final PCLCardData DATA = Register(Dominate.class).SetAttack(2, CardRarity.UNCOMMON);

    public Dominate()
    {
        super(DATA);

        Initialize(13, 0, 1);
        SetUpgrade(3, 0);

        SetDark();
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + PCLCombatStats.MatchingSystem.ResolveMeter.Resolve() * magicNumber;
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (CheckPrimaryCondition(true)) {
            amount *= 1.25f;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }
}