package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnTryGainResolveSubscriber;
import pinacolada.powers.EternalPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Origin extends EternalCard
{
    public static final PCLCardData DATA = Register(Origin.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal);

    public Origin()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(4, 0);

        SetDark();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        PCLActions.Bottom.StackPower(new OriginPower(player, 1));
        if (info.IsSynergizing) {
            PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(magicNumber);
        }
    }

    public static class OriginPower extends EternalPower implements OnTryGainResolveSubscriber
    {
        public OriginPower(AbstractCreature owner, int amount)
        {
            super(owner, Origin.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onTryGainResolve.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onTryGainResolve.Unsubscribe(this);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            ReducePower(1);
        }

        @Override
        public int OnTryGainResolve(AbstractCard card, AbstractPlayer p, int originalCost, boolean isActuallyGaining, boolean isFromMatch) {
            if (isFromMatch && isActuallyGaining && PCLGameUtilities.IsMismatch(card, PCLCombatStats.MatchingSystem.GetActiveMeter().GetCurrentAffinity()) && originalCost < 0) {
                ReducePower(1);
                return 0;
            }
            return originalCost;
        }
    }
}