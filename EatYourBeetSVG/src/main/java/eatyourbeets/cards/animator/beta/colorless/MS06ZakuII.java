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
import eatyourbeets.utilities.JUtils;

public class MS06ZakuII extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MS06ZakuII.class).SetPower(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetMaxCopies(1).SetMultiformData(2).SetSeries(CardSeries.Gundam);
    private static final UpgradedHand blight = new UpgradedHand();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(blight.name, GR.Animator.Strings.CardMods.HandSize);

    public MS06ZakuII()
    {
        super(DATA);

        Initialize(0, 0, 3, 30);

        SetAffinity_Red(1);
        SetAffinity_Silver(1);

        SetAffinityRequirement(Affinity.General, 12);
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

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        GameActions.Bottom.StackPower(new MS06ZakuIIPower(p, this.magicNumber));

        if (CheckSpecialCondition(true) && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                GameUtilities.IncreaseHandSizePermanently(hb.cX, hb.cY);
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return !player.hasBlight(UpgradedHand.ID) && CheckAffinity(Affinity.General) && JUtils.Find(Affinity.Extended(), a -> CombatStats.Affinities.GetPowerAmount((Affinity) a) > secondaryValue) != null;
    }

    public static class MS06ZakuIIPower extends AnimatorPower implements OnReloadPreDiscardSubscriber
    {
        public MS06ZakuIIPower(AbstractPlayer owner, int amount)
        {
            super(owner, MS06ZakuII.DATA);

            Initialize(amount);
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
                updateDescription();
                return card.makeSameInstanceOf();
            }
            return null;
        }
    }
}