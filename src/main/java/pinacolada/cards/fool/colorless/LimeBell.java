package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class LimeBell extends FoolCard
{
    public static final PCLCardData DATA = Register(LimeBell.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.AccelWorld);

    public LimeBell()
    {
        super(DATA);

        Initialize(0, 8, 4, 1);
        SetUpgrade(0, 2, 2, 0);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block)
        .AddCallback(() ->
        {
            int toConvert = Math.min(magicNumber, player.currentBlock);
            if (toConvert > 0)
            {
                player.loseBlock(toConvert, true);
                PCLActions.Bottom.GainTemporaryHP(toConvert);
            }
        });

        PCLActions.Bottom.Reload(name, cards -> PCLActions.Bottom.GainInvocation(cards.size() * secondaryValue));
    }
}