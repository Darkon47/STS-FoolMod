package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.interfaces.subscribers.OnTryGainResolveSubscriber;
import pinacolada.powers.EternalPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class ForcedComposure extends EternalCard
{
    public static final PCLCardData DATA = Register(ForcedComposure.class).SetAttack(1, CardRarity.COMMON);

    public ForcedComposure()
    {
        super(DATA);

        Initialize(0, 0, 5, 2);
        SetUpgrade(0, 0);

        SetLight();
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(magicNumber);
        PCLActions.Bottom.StackPower(new ForcedComposurePower(player, 2));
    }

    public static class ForcedComposurePower extends EternalPower implements OnTryGainResolveSubscriber
    {
        public ForcedComposurePower(AbstractCreature owner, int amount)
        {
            super(owner, ForcedComposure.DATA);

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
        public int OnTryGainResolve(AbstractCard card, AbstractPlayer p, int originalCost, boolean isActuallyGaining) {
            return Math.min(originalCost, 0);
        }
    }
}