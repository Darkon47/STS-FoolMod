package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.utilities.GameActions;

public class Kanaria_Pizzicato extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kanaria_Pizzicato.class)
    		.SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public Kanaria_Pizzicato()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);
        
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText("1", Settings.CREAM_COLOR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(1);
        boolean hasAether = false;
        for (AbstractOrb orb : p.orbs)
            if (Aether.ORB_ID.equals(orb.ID))
            {
                hasAether = true;
            }

        if (hasAether)
            GameActions.Bottom.Draw(magicNumber);
        else
            GameActions.Bottom.ChannelOrb(new Aether(), true);

        GameActions.Bottom.DiscardFromHand(name, 1, false);
    }
}

// If you have Aether Orb: Draw !M! cards, then discard 1 card. Otherwise: Channel 1 Aether.