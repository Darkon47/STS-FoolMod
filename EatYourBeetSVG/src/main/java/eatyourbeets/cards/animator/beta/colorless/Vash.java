package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class Vash extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vash.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Trigun);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Vash()
    {
        super(DATA);

        Initialize(3, 10, 1, 0);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);

        SetLoyal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.GUNSHOT).SetSoundPitch(0.5f, 0.7f);
        GameActions.Bottom.GainBlock(block);


        GameActions.Bottom.Reload(name, cards -> {
            GameActions.Bottom.StackPower(new VashPower(p, cards.size() * magicNumber));
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_Vash(CardType.ATTACK, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.SKILL, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.POWER, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.CURSE, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.STATUS, this, cards));
            }
            choices.Select(1, m);
        });

    }

    public static class VashPower extends AnimatorPower
    {
        public VashPower(AbstractCreature owner, int amount)
        {
            super(owner, Vash.DATA);

            Initialize(amount);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            if (card.type.equals(CardType.ATTACK)) {
                card.baseDamage += amount;
                RemovePower();
            }
        }
    }

    protected static class GenericEffect_Vash extends GenericEffect
    {
        private final CardType cardType;
        private final AnimatorCard source;
        private final ArrayList<AbstractCard> cards;


        public GenericEffect_Vash(CardType cardType, AnimatorCard source, ArrayList<AbstractCard> cards)
        {
            this.cardType = cardType;
            this.source = source;
            this.cards = cards;
        }

        @Override
        public String GetText()
        {
            return JUtils.Format("#y{0}", StringUtils.capitalize(cardType.toString()));
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            if (p.drawPile.size() > 0)
            {
                AbstractCard topCard = p.drawPile.getTopCard();
                if (topCard.type.equals(cardType))
                {
                    GameActions.Bottom.PlayCard(topCard,m);
                }
                else {
                    GameActions.Bottom.Exhaust(topCard);
                    GameActions.Bottom.Exhaust(source);
                }
            }
        }
    }
}