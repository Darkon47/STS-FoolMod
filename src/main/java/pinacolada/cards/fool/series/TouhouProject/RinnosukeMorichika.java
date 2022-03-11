package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class RinnosukeMorichika extends FoolCard
{
    public static final PCLCardData DATA = Register(RinnosukeMorichika.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage(true);

    public RinnosukeMorichika()
    {
        super(DATA);

        Initialize(0, 6, 3, 1);
        SetUpgrade(0, 1, 0, 0);
        SetAffinity_Blue(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 7);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        

        final CardGroup[] choices = upgraded ? new CardGroup[] {player.hand,player.drawPile,player.discardPile,player.exhaustPile} : new CardGroup[] {player.hand};
        PCLActions.Bottom.SelectFromPile(name, magicNumber,  choices)
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (card instanceof PCLCard) {
                    PCLGameUtilities.AddAffinities(((PCLCard) card).affinities, true);
                    if (info.IsSynergizing || CheckAffinity(PCLAffinity.General)) {
                        for (PCLAffinity af : PCLAffinity.Extended()) {
                            if (PCLGameUtilities.GetPCLAffinityLevel(card, af, true) > 0) {
                                PCLActions.Bottom.StackAffinityPower(af, 1, false);
                            }
                        }
                    }
                }
                if (PCLGameUtilities.IsHindrance(card)) {
                    PCLActions.Bottom.GainSorcery(secondaryValue);
                }
            }
        });
    }
}

