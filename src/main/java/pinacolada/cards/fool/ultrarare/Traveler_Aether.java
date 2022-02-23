package pinacolada.cards.fool.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.cards.fool.special.Traveler_Wish;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Traveler_Aether extends FoolCard_UltraRare
{
    public static final PCLCardData DATA = Register(Traveler_Aether.class)
            .SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColorless()
            .SetSeries(CardSeries.GenshinImpact)
            .SetTraits(PCLCardTrait.Protagonist)
            .PostInitialize(data -> data.AddPreview(new Traveler_Wish(), false));
    public static final int UNIQUE_ORB_THRESHOLD = 3;


    public Traveler_Aether()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Star(1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Air, secondaryValue).AddCallback(() -> {
            int orbsInduced = 0;

            RandomizedList<AbstractOrb> orbList = new RandomizedList<>(PCLJUtils.Filter(player.orbs, PCLGameUtilities::IsCommonOrb));
            if (orbList.Size() > 0) {
                for (int i = 0; i < magicNumber; i++) {
                    PCLActions.Bottom.InduceOrb(orbList.Retrieve(rng,false).makeCopy(), true);
                }
            }

            if (CheckSpecialCondition(false) && CombatStats.TryActivateSemiLimited(cardID)) {
                PCLActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLGameUtilities.GetUniqueOrbsCount() >= UNIQUE_ORB_THRESHOLD;
    }
}