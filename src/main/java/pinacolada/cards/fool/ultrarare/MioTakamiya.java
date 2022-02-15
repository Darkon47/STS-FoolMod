package pinacolada.cards.fool.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.cards.fool.series.DateALive.ShidoItsuka;
import pinacolada.powers.FoolPower;
import pinacolada.powers.fool.MagusFormPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class MioTakamiya extends FoolCard_UltraRare
{
    public static final PCLCardData DATA = Register(MioTakamiya.class).SetPower(3, CardRarity.SPECIAL).SetColorless().SetSeries(CardSeries.DateALive)
            .PostInitialize(data -> data.AddPreview(new ShidoItsuka(), false));

    public MioTakamiya()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetAffinity_Light(1, 0, 0);
        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new MagusFormPower(p, this.magicNumber));
        PCLActions.Bottom.StackPower(new MioTakamiyaPower(p, magicNumber));
    }

    public static class MioTakamiyaPower extends FoolPower
    {
        public MioTakamiyaPower(AbstractPlayer owner, int amount)
        {
            super(owner, MioTakamiya.DATA);

            this.amount = amount;

            Initialize(amount);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (this.amount > 0) {
                final PCLCard card = PCLJUtils.SafeCast(usedCard, PCLCard.class);
                if (card != null && card.series != null)
                {
                    for (AbstractCard c : player.drawPile.group) {
                        IncreaseSameSeriescard(card, c);
                    }
                    for (AbstractCard c : player.discardPile.group) {
                        IncreaseSameSeriescard(card, c);
                    }
                    for (AbstractCard c : player.hand.group) {
                        IncreaseSameSeriescard(card, c);
                    }
                    updateDescription();
                    flash();
                }
            }
        }

        private void IncreaseSameSeriescard(PCLCard playedCard, AbstractCard incoming) {
            if (PCLGameUtilities.IsSameSeries(playedCard, incoming)) {
                if (incoming.baseBlock > 0)
                {
                    PCLGameUtilities.IncreaseBlock(incoming, amount, false);
                }
                if (incoming.baseDamage > 0)
                {
                    PCLGameUtilities.IncreaseDamage(incoming, amount, false);
                }
            }
        }
    }

}