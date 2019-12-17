package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.OnStartOfTurnSubscriber;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Emilia extends AnimatorCard implements OnStartOfTurnSubscriber, Spellcaster
{
    public static final String ID = Register(Emilia.class.getSimpleName(), EYBCardBadge.Special);

    public Emilia()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 2);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
        SetSynergy(Synergies.ReZero);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 6);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Frost(), true);
        }

        PlayerStatistics.onStartOfTurn.Subscribe((Emilia)makeStatEquivalentCopy());
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            SetEvokeOrbCount(magicNumber);
        }
    }

    @Override
    public void OnStartOfTurn()
    {
        GameEffects.Queue.ShowCardBriefly(this);

        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            if (orb != null && Frost.ORB_ID.equals(orb.ID))
            {
                GameActions.Bottom.ChannelOrb(new Lightning(), true);
            }
        }

        PlayerStatistics.onStartOfTurn.Unsubscribe(this);
    }
}