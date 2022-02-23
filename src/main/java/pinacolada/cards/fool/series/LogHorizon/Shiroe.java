package pinacolada.cards.fool.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.FoolPower;
import pinacolada.utilities.PCLActions;

public class Shiroe extends FoolCard
{
    public static final int MINIMUM_AFFINITY = 4;
    public static final PCLCardData DATA = Register(Shiroe.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(2)
            .SetTraits(PCLCardTrait.Protagonist)
            .SetSeriesFromClassPackage();

    public Shiroe()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1,0,2);
        SetAffinity_Orange(1,0,1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Cycle(name, magicNumber).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                if (c instanceof PCLCard) {
                    PCLActions.Bottom.AddAffinities(((PCLCard) c).affinities);
                }
            }
        });
        PCLActions.Bottom.StackPower(new ShiroePower(p, secondaryValue));
    }

    public static class ShiroePower extends FoolPower
    {
        public ShiroePower(AbstractPlayer owner, int amount)
        {
            super(owner, Shiroe.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            PCLActions.Bottom.TryChooseSpendAffinity(name, MINIMUM_AFFINITY).AddConditionalCallback(() -> {
                PCLActions.Bottom.ApplyRippled(TargetHelper.Enemies(), amount);
            });
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            PCLActions.Bottom.RemovePower(owner, owner, this);
        }
    }
}