package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.JotaroKujo_StarPlatinum;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class JotaroKujo extends FoolCard
{
    public static final PCLCardData DATA = Register(JotaroKujo.class)
            .SetSkill(3, CardRarity.RARE, PCLCardTarget.None)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.Jojo)
            .SetMultiformData(2, false)
            .PostInitialize(data -> data.AddPreview(new JotaroKujo_StarPlatinum(), false));

    private int turns;

    public JotaroKujo()
    {
        super(DATA);

        Initialize(0, 18, 1, 0);

        SetAffinity_Red(1,0,2);
        SetAffinity_Light(1, 0, 1);

        SetSoul(6, 0, JotaroKujo_StarPlatinum::new);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        AddScaling(PCLAffinity.Red, 1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetUpgrade(0, 0, 1, 0);
            }
            else {
                SetUpgrade(0, 1, 0, 0);
                SetInnate(true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        

        PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile)
        .SetOptions(true, false)
            .SetFilter(c -> c.cost >= 2)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    PCLGameUtilities.Retain(c);
                    PCLCard eCard = PCLJUtils.SafeCast(c, PCLCard.class);
                    if (eCard != null && PCLGameUtilities.GetPCLAffinityLevel(eCard, PCLAffinity.General, true) > 0)
                    {
                        PCLActions.Delayed.SelectFromHand(name, magicNumber, false)
                                .SetFilter(ca -> eCard != ca)
                                .AddCallback(cards2 ->
                                {
                                    for (AbstractCard c2 : cards2)
                                    {
                                        for (PCLCardAffinity cardAffinity : eCard.affinities.GetCardAffinities(true)) {
                                            PCLActions.Top.IncreaseScaling(c2, cardAffinity.type, cardAffinity.scaling);
                                        }
                                        if (eCard.affinities.Star != null) {
                                            PCLActions.Top.IncreaseScaling(c2, PCLAffinity.Star, eCard.affinities.Star.scaling);
                                        }
                                    }
                                });
                    }
                }
            });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}