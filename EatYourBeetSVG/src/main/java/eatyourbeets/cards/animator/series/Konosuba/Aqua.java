package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActions;

public class Aqua extends AnimatorCard
{
    private boolean transformed = false;

    public static final String ID = Register(Aqua.class.getSimpleName());

    public Aqua() 
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2, 3);

        SetHealing(true);
        SetSynergy(Synergies.Konosuba);

        if (InitializingPreview())
        {
            Aqua copy = new Aqua(); // InitializingPreview will be true only once
            copy.SetTransformed(true);
            cardData.InitializePreview(copy, true);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (upgraded && transformed)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded && transformed)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (!transformed)
        {
            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.Callback(__ -> SetTransformed(true));
        }
        else
        {
            GameActions.Bottom.VFX(new RainbowCardEffect());
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade(false))
        {
            upgradeMagicNumber(1);
            SetTransformed(transformed);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Aqua other = (Aqua) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
    }

    private void SetTransformed(boolean value)
    {
        transformed = value;

        if (transformed)
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID + "2"));
            cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], true);
            transformed = true;
        }
        else
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID));
            cardText.OverrideDescription(null, true);
            transformed = false;
        }
    }
}