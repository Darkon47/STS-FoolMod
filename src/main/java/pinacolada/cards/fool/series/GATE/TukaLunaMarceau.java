package pinacolada.cards.fool.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.ThrowingKnife;
import pinacolada.utilities.PCLActions;

public class TukaLunaMarceau extends FoolCard
{
    public static final PCLCardData DATA = Register(TukaLunaMarceau.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage().PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 2, 3);
        SetUpgrade(0, 3);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Light, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.currentBlock <= 0)
        {
            PCLActions.Bottom.Draw(1);
        }

        PCLActions.Bottom.GainBlock(block);

        if (TrySpendAffinity(PCLAffinity.Light, PCLAffinity.Green))
        {
            PCLActions.Bottom.CreateThrowingKnives(1);
        }
    }
}