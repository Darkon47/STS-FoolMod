package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Priestess extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Priestess.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL, true)
            .SetSeriesFromClassPackage();

    public Priestess()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 2, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(2, 0, 1);

        SetAffinityRequirement(Affinity.Orange, 8);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);

        if (info.IsSynergizing || CheckAffinity(Affinity.Orange))
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsHindrance).AddCallback(
                    cards -> {
                        if (cards.size() > 0) {
                            GameActions.Bottom.ReducePower(player, FrailPower.POWER_ID,1);
                            GameActions.Bottom.ReducePower(player, VulnerablePower.POWER_ID,1);
                        }
                    }
            );
        }
    }
}