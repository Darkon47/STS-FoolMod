package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.curse.Curse_GriefSeed;
import pinacolada.cards.fool.series.MadokaMagica.SayakaMiki;
import pinacolada.utilities.PCLActions;

public class MamiTomoe_Candeloro extends FoolCard
{
    public static final PCLCardData DATA = Register(MamiTomoe_Candeloro.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MamiTomoe_Candeloro()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnPurge() {
        PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.FetchFromPile(name, secondaryValue, p.discardPile).SetOptions(false, false).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                PCLActions.Bottom.Motivate(c, c.costForTurn);
            }
        });
        PCLActions.Delayed.Add(new CreateRandomCurses(magicNumber, p.hand));
    }
}
