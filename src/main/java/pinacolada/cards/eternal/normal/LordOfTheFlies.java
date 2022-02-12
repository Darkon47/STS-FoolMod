package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.vfx.megacritCopy.HemokinesisEffect2;
import pinacolada.interfaces.subscribers.OnNotSynergySubscriber;
import pinacolada.powers.EternalPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class LordOfTheFlies extends EternalCard
{
    public static final PCLCardData DATA = Register(LordOfTheFlies.class).SetPower(3, CardRarity.RARE);

    public LordOfTheFlies()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);

        SetDark();
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new LordOfTheFliesPower(player, magicNumber, secondaryValue));
    }

    public static class LordOfTheFliesPower extends EternalPower implements OnSynergySubscriber, OnNotSynergySubscriber
    {
        protected int secondaryAmount;
        public LordOfTheFliesPower(AbstractCreature owner, int amount, int secondaryAmount)
        {
            super(owner, LordOfTheFlies.DATA);

            Initialize(amount);
            this.secondaryAmount = secondaryAmount;
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, secondaryAmount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onNotSynergy.Subscribe(this);
            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onNotSynergy.Unsubscribe(this);
            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard abstractCard) {
            PCLActions.Bottom.TakeDamage(secondaryAmount, AbstractGameAction.AttackEffect.NONE);
            flash();
        }

        @Override
        public void OnNotSynergy(AbstractCard card) {
            PCLActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                    .SetDamageEffect(enemy ->
                    {
                        PCLGameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
                        return 0f;
                    });
            PCLActions.Bottom.GainTemporaryHP(amount);
            flash();
        }
    }
}