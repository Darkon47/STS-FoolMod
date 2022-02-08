package pinacolada.cards.fool.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.stances.pcl.MightStance;
import pinacolada.stances.pcl.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class SakuyaOhtori extends FoolCard
{
    public static final PCLCardData DATA = Register(SakuyaOhtori.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.Normal).SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public SakuyaOhtori()
    {
        super(DATA);

        Initialize(0, 3, 2, 3);
        SetUpgrade(0, 2, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 1);

        SetHitCount(2);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(hitCount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<hitCount; i++)
        {
            PCLActions.Bottom.GainBlock(block);
        }

        if (PCLGameUtilities.InStance(MightStance.STANCE_ID))
        {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
            buffs = CombatStats.GetCombatData(cardID, new HashMap<>());

            PCLActions.Bottom.SelectFromHand(name, 1, false)
                    .SetOptions(false, false, false)
                    .SetMessage(PGR.PCL.Strings.HandSelection.GenericBuff)
                    .SetFilter(c -> c instanceof PCLCard && !PCLGameUtilities.IsHindrance(c) && buffs.getOrDefault(c.uuid, 0) < secondaryValue && (c.baseDamage >= 0 || c.baseBlock >= 0))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Blue, secondaryValue);
                            PCLJUtils.IncrementMapElement(buffs, c.uuid, secondaryValue);
                            c.flash();
                        }
                    });

        }
        else
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
            PCLActions.Bottom.ApplyBlinded(TargetHelper.Normal(m), magicNumber);
        }
    }
}