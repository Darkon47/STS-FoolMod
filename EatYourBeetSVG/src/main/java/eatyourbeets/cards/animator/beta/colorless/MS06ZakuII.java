package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.animator.UpgradedHand;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnReloadPreDiscardSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MS06ZakuII extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MS06ZakuII.class).SetPower(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Gundam);
    public static final String[] TEXT = GR.GetUIStrings(GR.Animator.CreateID("CardMods")).TEXT;
    private static final UpgradedHand blight = new UpgradedHand();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(blight.name, TEXT[0]);

    public MS06ZakuII()
    {
        super(DATA);

        Initialize(0, 0, 3, 12);
        SetUpgrade(0, 0, 1);
        SetRetainOnce(true);

        SetAffinity_Red(1);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(tooltip);
        }
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.StackPower(new MS06ZakuIIPower(p, this.magicNumber));

        if (!player.hasBlight(UpgradedHand.ID) && CheckAffinity(Affinity.General)) {
            for (Affinity t : Affinity.Basic()) {
                if (CombatStats.Affinities.GetPowerThreshold(t) > secondaryValue && CombatStats.TryActivateLimited(cardID)) {
                    GameUtilities.ObtainBlight(hb.cX, hb.cY, new UpgradedHand());
                    break;
                }
            }
        }


    }

    public static class MS06ZakuIIPower extends AnimatorPower implements OnReloadPreDiscardSubscriber
    {
        public MS06ZakuIIPower(AbstractPlayer owner, int amount)
        {
            super(owner, MS06ZakuII.DATA);

            this.amount = amount;
            CombatStats.onReloadPreDiscard.Subscribe(this);
            updateDescription();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onReloadPreDiscard.Unsubscribe(this);
        }


        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public AbstractCard OnReloadPreDiscard(AbstractCard card)
        {
            if (amount > 0)
            {
                this.amount -= 1;
                return card.makeSameInstanceOf();
            }
            return null;
        }
    }
}