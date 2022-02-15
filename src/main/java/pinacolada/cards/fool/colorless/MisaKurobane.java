package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.MisaKurobane_Yusarin;
import pinacolada.orbs.pcl.Fire;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class MisaKurobane extends FoolCard
{
    public static final PCLCardData DATA = Register(MisaKurobane.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.Charlotte)
            .PostInitialize(data -> data.AddPreview(new MisaKurobane_Yusarin(), false));

    public MisaKurobane()
    {
        super(DATA);

        Initialize(0, 0,1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);

        SetExhaust(true);
        SetEvokeOrbCount(1);

        SetAffinityRequirement(PCLAffinity.Light, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrb(new Fire());
        PCLActions.Bottom.Draw(magicNumber);

        if (TrySpendAffinity(PCLAffinity.Light))
        {
            PCLActions.Bottom.MakeCardInDrawPile(new MisaKurobane_Yusarin());
        }
    }
}