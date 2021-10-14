package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.curse.SayakaMiki_Oktavia;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class SayakaMiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SayakaMiki.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new SayakaMiki_Oktavia(), true);
            });

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
        SetSoul(4, 0, SayakaMiki_Oktavia::new);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int aff = CombatStats.Affinities.GetAffinityLevel(Affinity.Blue, true);
        magicNumber = aff + baseMagicNumber;
        isMagicNumberModified = magicNumber > baseMagicNumber;
        SetAffinityRequirement(Affinity.Blue, aff);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        TrySpendAffinity(Affinity.Blue);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.ChannelOrb(new Frost());

        GameActions.Bottom.StackAffinityPower(Affinity.Blue, secondaryValue, upgraded);

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
