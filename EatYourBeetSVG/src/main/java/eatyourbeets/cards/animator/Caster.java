package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class Caster extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Caster.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Caster()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 2);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.Fate);
    }


    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            if (orb != null && Dark.ORB_ID.equals(orb.ID))
            {
                GameActions.Bottom.Add(new EvokeSpecificOrbAction(orb));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ChannelOrb(new Dark(), true);
        GameActions.Bottom.StealStrength(m, magicNumber, true);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}