package pinacolada.cards.fool.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import static eatyourbeets.resources.GR.Enums.CardTags.HASTE;

public class TsurugaMeisai extends FoolCard
{
    public static final PCLCardData DATA = Register(TsurugaMeisai.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public TsurugaMeisai()
    {
        super(DATA);

        Initialize(0, 2, 6, 2);
        SetUpgrade(0, 2, 2);

        SetAffinity_Light(1);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, magicNumber));
        PCLActions.Bottom.CreateThrowingKnives(secondaryValue, player.drawPile).AddCallback(c -> {
            PCLGameUtilities.SetCardTag(c, HASTE, true);
        });
        PCLActions.Bottom.DiscardFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .AddCallback(cards -> {
                   if (cards.size() > 0) {
                       PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, secondaryValue));
                   }
                });
    }
}