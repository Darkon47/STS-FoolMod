package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.basic.RemoveBlock;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Steelbreaker extends EternalCard
{
    public static final PCLCardData DATA = Register(Steelbreaker.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Piercing);

    public Steelbreaker()
    {
        super(DATA);

        Initialize(14, 0, 2, 1);
        SetUpgrade(4, 0);

        SetDark();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        boolean hasBlock = m.currentBlock > 0;
        PCLActions.Bottom.Add(new RemoveBlock(m, p)).SetVFX(true, true);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Normal(m), hasBlock ? magicNumber + secondaryValue : magicNumber);
        PCLActions.Last.Add(new RemoveBlock(m, p)).SetVFX(true, false);
    }
}