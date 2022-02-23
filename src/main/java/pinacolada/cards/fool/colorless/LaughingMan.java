package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnFirstSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.FoolPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class LaughingMan extends FoolCard implements OnEndOfTurnFirstSubscriber, OnStartOfTurnSubscriber
{
    public static final PCLCardData DATA = Register(LaughingMan.class).SetSkill(0, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(1)
            .SetColorless()
            .SetTraits(PCLCardTrait.Machine)
            .SetSeries(CardSeries.GhostInTheShell);
    private static final CardEffectChoice choices = new CardEffectChoice();
    private CardType cardType;

    public LaughingMan()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetAutoplay(true);
        SetExhaust(true);

        SetAffinity_Star(1, 0, 0);
    }

    public void OnUpgrade() {
        SetPermanentHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLCombatStats.onEndOfTurn.SubscribeOnce(this);
    }

    @Override
    public void OnEndOfTurnFirst(boolean isPlayer) {
        cardType = null;
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_LaughingMan(CardType.ATTACK, this));
            choices.AddEffect(new GenericEffect_LaughingMan(CardType.SKILL, this));
            choices.AddEffect(new GenericEffect_LaughingMan(CardType.POWER, this));
            choices.AddEffect(new GenericEffect_LaughingMan(CardType.CURSE, this));
            choices.AddEffect(new GenericEffect_LaughingMan(CardType.STATUS, this));
        }
        choices.Select(1, null);
    }

    @Override
    public void OnStartOfTurn()
    {
        super.OnStartOfTurn();
        if (cardType != null) {
            PCLGameEffects.Queue.ShowCardBriefly(this);
            PCLActions.Bottom.StackPower(new LaughingManPower(player, this.magicNumber, cardType));
        }
    }

    public static class LaughingManPower extends FoolPower
    {
        private final CardType cardType;
        public LaughingManPower(AbstractPlayer owner, int amount, CardType cardType)
        {
            super(owner, LaughingMan.DATA);

            this.amount = amount;
            this.cardType = cardType;
            this.priority = 99;

            updateDescription();
        }

        public void onCardDraw(AbstractCard card) {
            if (!card.type.equals(cardType) && !(card instanceof Necronomicurse)) {
                PCLActions.Bottom.Exhaust(card);
                PCLActions.Bottom.Draw(1);
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
            description = FormatDescription(0, amount, cardType.toString());
        }
    }

    protected static class GenericEffect_LaughingMan extends GenericEffect
    {
        private final CardType cardType;
        private final LaughingMan source;


        public GenericEffect_LaughingMan(CardType cardType, LaughingMan source)
        {
            this.cardType = cardType;
            this.source = source;
        }

        @Override
        public String GetText()
        {
            return PGR.PCL.Strings.GridSelection.CardsInPile(StringUtils.capitalize(cardType.toString().toLowerCase()), PCLJUtils.Count(player.drawPile.group, c -> c.type == cardType));
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLCombatStats.onStartOfTurn.SubscribeOnce(source);
            source.cardType = cardType;
        }
    }
}