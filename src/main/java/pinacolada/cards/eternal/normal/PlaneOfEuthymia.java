package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.actions.orbs.TriggerOrbPassiveAbility;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.EternalPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class PlaneOfEuthymia extends EternalCard
{
    public static final PCLCardData DATA = Register(PlaneOfEuthymia.class).SetPower(3, CardRarity.RARE);

    public PlaneOfEuthymia()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);

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
        PCLActions.Bottom.StackPower(new PlaneOfEuthymiaPower(player, magicNumber));
    }

    public static class PlaneOfEuthymiaPower extends EternalPower implements OnSynergySubscriber, OnShuffleSubscriber
    {
        public PlaneOfEuthymiaPower(AbstractCreature owner, int amount)
        {
            super(owner, PlaneOfEuthymia.DATA);

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

            PCLCombatStats.onShuffle.Subscribe(this);
            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onShuffle.Unsubscribe(this);
            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard abstractCard) {
            PCLActions.Bottom.Callback(new TriggerOrbPassiveAbility(1));
            flash();
        }

        @Override
        public void OnShuffle(boolean b) {
            PCLActions.Bottom.ChannelOrb(new Dark());
            flash();
        }
    }
}