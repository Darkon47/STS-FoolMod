package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.relics.MedicalKit;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Doubt;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Holou extends PCLCard
{
    public static final PCLCardData DATA = Register(Holou.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Doubt(), false));

    public Holou()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Blue(1);

        SetEthereal(true);
    }

    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new HolouPower(p, magicNumber));
    }

    public static class HolouPower extends PCLPower implements OnShuffleSubscriber, OnCardCreatedSubscriber
    {
        public HolouPower(AbstractCreature owner, int amount)
        {
            super(owner, Holou.DATA);

            this.amount = amount;

            Initialize(amount);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            CheckCards();
            PCLCombatStats.onCardCreated.Subscribe(this);
            PCLCombatStats.onShuffle.Subscribe(this);
            CombatStats.SetCombatData(BlueCandle.ID, true);
            CombatStats.SetCombatData(MedicalKit.ID, true);
        }

        @Override
        public void onRemove()
        {
            PCLCombatStats.onCardCreated.Unsubscribe(this);
            PCLCombatStats.onShuffle.Unsubscribe(this);
            CombatStats.SetCombatData(BlueCandle.ID, false);
            CombatStats.SetCombatData(MedicalKit.ID, false);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            if (PCLGameUtilities.IsHindrance(card)) {
                PCLActions.Bottom.LoseHP(1, AbstractGameAction.AttackEffect.NONE);
                PCLActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), amount);
                PCLActions.Last.Exhaust(card);
                flash();
            }
        }


        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            PCLActions.Last.MakeCardInDrawPile(new Curse_Doubt())
                    .AddCallback(c -> {
                        if (c instanceof PCLCard) {
                            ((PCLCard) c).SetUnplayable(false);
                        }
                    });
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle) {
            ModifyCard(card, startOfBattle);
            CheckCards();
        }

        protected void CheckCards() {
            final ArrayList<AbstractCard> cards = new ArrayList<>(player.drawPile.group);
            cards.addAll(player.hand.group);
            cards.addAll(player.discardPile.group);
            cards.addAll(player.exhaustPile.group);

            for (AbstractCard c : cards)
            {
                ModifyCard(c, false);
            }
        }

        protected void ModifyCard(AbstractCard card, boolean startOfBattle) {
            if (PCLGameUtilities.IsHindrance(card) && card instanceof PCLCard) {
                PCLActions.Last.ModifyAllInstances(card.uuid, c -> ((PCLCard)c).SetUnplayable(false));
            }
        }
    }
}