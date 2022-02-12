package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnNotSynergySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Erosion extends EternalCard implements OnNotSynergySubscriber
{
    public static final PCLCardData DATA = Register(Erosion.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Piercing);

    public Erosion()
    {
        super(DATA);

        Initialize(7, 0, 4);

        SetDark();
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        SetExhaust(false);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onNotSynergy.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.LIGHTNING);
    }

    @Override
    public void OnNotSynergy(AbstractCard card) {
        if (player.hand.contains(this) && PCLGameUtilities.IsMismatch(card, PCLCombatStats.MatchingSystem.GetActiveMeter().GetCurrentAffinity())) {
            PCLGameUtilities.ModifyDamage(this, magicNumber, false);
        }
    }
}