package pinacolada.cards.fool.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.curse.Curse_GriefSeed;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MifuyuAsuza extends FoolCard
{
    public static final PCLCardData DATA = Register(MifuyuAsuza.class)
            .SetSkill(-1, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MifuyuAsuza()
    {
        super(DATA);

        Initialize(0, 2, 0);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1, 0, 2);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.General, 9);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        int amount = PCLGameUtilities.UseXCostEnergy(this) + magicNumber;
        PCLActions.Bottom.GainOrbSlots(amount);
        for (int i = 0; i < amount; i++) {
            PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
        }

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.RandomCommonHelper(), amount);
        });
    }
}