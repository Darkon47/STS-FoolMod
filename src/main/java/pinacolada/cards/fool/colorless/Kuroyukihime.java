package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Kuroyukihime_BlackLotus;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class Kuroyukihime extends FoolCard
{
    public static final PCLCardData DATA = Register(Kuroyukihime.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.AccelWorld)
            .PostInitialize(data -> data.AddPreview(new Kuroyukihime_BlackLotus(), true));

    public Kuroyukihime()
    {
        super(DATA);

        Initialize(0, 1, 2, 1);
        SetCostUpgrade(-1);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Silver(1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.General, 8);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.GainBlur(secondaryValue);
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            PCLActions.Bottom.MakeCardInHand(new Kuroyukihime_BlackLotus())
            .SetUpgrade(CheckAffinity(PCLAffinity.General), false);
        });
    }
}