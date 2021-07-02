package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Oz extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Oz.class).SetPower(1, CardRarity.SPECIAL);

    public Oz()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetEthereal(true);
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }
        GameActions.Bottom.StackPower(new OzPower(p, this.secondaryValue));
    }

    public static class OzPower extends AnimatorPower
    {
        private int spellcastersPlayed;

        public OzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Oz.DATA);

            this.amount = amount;
            this.spellcastersPlayed = 0;

            updateDescription();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {

            super.onPlayCard(card, m);

            if (this.spellcastersPlayed < this.amount && card.tags.contains(SPELLCASTER)) {
                GameActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());
                this.spellcastersPlayed += 1;
                updateDescription();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            this.spellcastersPlayed = 0;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, this.spellcastersPlayed);
        }
    }
}