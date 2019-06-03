package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Air;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.TemporaryBiasPower;

public class Tatsumaki extends AnimatorCard
{
    public static final String ID = CreateFullID(Tatsumaki.class.getSimpleName());

    public Tatsumaki()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2,3);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Air(), true);

        PlayerStatistics.ApplyTemporaryFocus(p, p, magicNumber);

        if (HasActiveSynergy())
        {
            for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m1, new StrengthPower(m1, -this.secondaryValue), -this.secondaryValue);
                if (!m1.hasPower(ArtifactPower.POWER_ID))
                {
                    GameActionsHelper.ApplyPower(p, m1, new GainStrengthPower(m1, this.secondaryValue), this.secondaryValue);
                }
            }
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