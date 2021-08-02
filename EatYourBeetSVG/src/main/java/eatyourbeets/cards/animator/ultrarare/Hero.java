package eatyourbeets.cards.animator.ultrarare;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hero extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Hero.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GoblinSlayer);

    public Hero()
    {
        super(DATA);

        Initialize(8, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Light(2, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY)
        .AddCallback(c ->
        {
            if (GameUtilities.IsFatal(c, true) && CombatStats.TryActivateLimited(cardID))
            {
                Random rng = new Random(Settings.seed + (AbstractDungeon.actNum * 17) + (AbstractDungeon.floorNum * 23));
                if (rng.randomBoolean(0.4f))
                {
                    AbstractRelic.RelicTier tier;

                    int roll = rng.random(0, 99);
                    if (roll < 50)
                    {
                        tier = AbstractRelic.RelicTier.COMMON;
                    }
                    else if (roll < 82)
                    {
                        tier = AbstractRelic.RelicTier.UNCOMMON;
                    }
                    else
                    {
                        tier = AbstractRelic.RelicTier.RARE;
                    }

                    AbstractDungeon.getCurrRoom().addRelicToRewards(tier);
                }
            }
        });
        GameActions.Bottom.Draw(magicNumber);
    }
}