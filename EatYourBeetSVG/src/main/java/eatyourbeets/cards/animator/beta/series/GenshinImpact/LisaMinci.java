package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.animator.CounterAttackPower;
import eatyourbeets.utilities.GameActions;

public class LisaMinci extends AnimatorCard {
    public static final EYBCardData DATA = Register(LisaMinci.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public LisaMinci() {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(2, 0, 0);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Top.Scry(magicNumber)
                .AddCallback(cards -> {
                    for (AbstractCard card : cards)
                    {
                        switch (card.rarity) {
                            case RARE:
                                GameActions.Bottom.GainIntellect(1, upgraded);
                                GameActions.Bottom.GainLuck(1, upgraded);
                                break;
                            case UNCOMMON:
                                GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, secondaryValue));
                                break;
                            case BASIC:
                            case COMMON:
                                GameActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
                                break;
                        }
                    }
                });
    }
}