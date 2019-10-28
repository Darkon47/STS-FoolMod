package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;

public class Caster extends AnimatorCard
{
    public static final String ID = Register(Caster.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Caster()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 2);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            if (orb != null && Dark.ORB_ID.equals(orb.ID))
            {
                GameActionsHelper.AddToBottom(new EvokeSpecificOrbAction(orb));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Dark(), true);

        if (PlayerStatistics.UseArtifact(m))
        {
            PlayerStatistics.LoseTemporaryStrength(p, m, magicNumber);
            PlayerStatistics.GainTemporaryStrength(p, p, magicNumber);
        }
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