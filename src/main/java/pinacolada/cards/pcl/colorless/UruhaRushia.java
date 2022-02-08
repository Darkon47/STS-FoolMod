package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnModifyDamageLastSubscriber;
import eatyourbeets.utilities.GameUtilities;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class UruhaRushia extends PCLCard implements OnOrbApplyFocusSubscriber
{
    public static final PCLCardData DATA = Register(UruhaRushia.class).SetSkill(0, CardRarity.RARE, PCLCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Hololive);

    public UruhaRushia()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetRetainOnce(true);
        SetExhaust(true);

        SetAffinity_Blue(1);
    }

    public void OnUpgrade() {
        SetRetain(true);
        SetHaste(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onOrbApplyFocus.Subscribe(this);
        for (AbstractOrb orb : player.orbs) {
            if (GameUtilities.IsValidOrb(orb)) {
                orb.applyFocus();
                break;
            }
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractOrb orb : player.orbs) {
            if (GameUtilities.IsValidOrb(orb)) {
                orb.applyFocus();
                break;
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new UruhaRushiaPower(p, this.secondaryValue));
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        int index = player.orbs.indexOf(orb);
        if (player.hand.contains(this) && index == 0) {
            PCLGameUtilities.ModifyOrbTemporaryFocus(orb, magicNumber, true, false);
        }
    }

    public static class UruhaRushiaPower extends PCLPower implements OnModifyDamageLastSubscriber
    {
        public UruhaRushiaPower(AbstractPlayer owner, int amount)
        {
            super(owner, UruhaRushia.DATA);

            this.amount = amount;
            this.priority = 99;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onModifyDamageLast.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onModifyDamageLast.Unsubscribe(this);
        }

        public int OnModifyDamageLast(AbstractCreature target, DamageInfo info, int damage) {
            if (target != player || !PCLGameUtilities.IsPlayerTurn(true) || info.type == DamageInfo.DamageType.NORMAL && info.owner != null && !info.owner.isPlayer) {
                return damage;
            } else {
                this.flash();
                return 0;
            }
        }

        @Override
        public void atEndOfRound()
        {
            ReducePower(1);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}