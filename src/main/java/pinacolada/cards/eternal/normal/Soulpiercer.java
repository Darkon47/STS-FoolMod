package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Soulpiercer extends EternalCard
{
    public static final PCLCardData DATA = Register(Soulpiercer.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Piercing);

    public Soulpiercer()
    {
        super(DATA);

        Initialize(14, 0, 6, 13);
        SetUpgrade(4, 0);

        SetDark();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.DealCardDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .forEach(d -> d.AddCallback(m, (__, enemy) -> {
                    if (PCLGameUtilities.IsFatal(enemy, true)) {
                        PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(magicNumber);
                    }}));

        if (IsMismatching(info) && PCLCombatStats.MatchingSystem.ResolveMeter.TrySpendResolve(magicNumber)) {
            this.baseDamage += secondaryValue;
        }
    }
}