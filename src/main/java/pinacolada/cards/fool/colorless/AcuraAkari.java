package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.ThrowingKnife;
import pinacolada.powers.temporary.TemporaryEnvenomPower;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class AcuraAkari extends FoolCard
{
    public static final PCLCardData DATA = Register(AcuraAkari.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetMaxCopies(3)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public AcuraAkari()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 0);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .AddCallback(() -> PCLActions.Bottom.CreateThrowingKnives(magicNumber).SetUpgrade(upgraded));

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }
}