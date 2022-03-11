package pinacolada.cards.fool.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class LizardPriest extends FoolCard
{
    public static final PCLCardData DATA = Register(LizardPriest.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetTraits(PCLCardTrait.Reptile);
    public static final int TEMPORARY_THORNS = 2;
    public static final int BLOCK_NEXT_TURN = 3;


    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 4, BLOCK_NEXT_TURN, TEMPORARY_THORNS);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> PCLGameUtilities.CanRetain(c) && c.type == CardType.ATTACK)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        PCLGameUtilities.Retain(c);
                        PCLCard ec = PCLJUtils.SafeCast(c, PCLCard.class);
                        if (ec != null) {
                            switch (ec.attackType) {
                                case Brutal:
                                    PCLActions.Bottom.GainInspiration(1);
                                    break;
                                case Ranged:
                                    PCLActions.Bottom.GainTemporaryThorns(TEMPORARY_THORNS);
                                    break;
                                default:
                                    PCLActions.Bottom.StackPower(new NextTurnBlockPower(player, BLOCK_NEXT_TURN));
                                    break;
                            }
                        }
                        else {
                            PCLActions.Bottom.StackPower(new NextTurnBlockPower(player, BLOCK_NEXT_TURN));
                        }
                    }
                });
    }
}