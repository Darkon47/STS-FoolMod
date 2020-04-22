package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Shiroe extends AnimatorCard {
    public static final EYBCardData DATA = Register(Shiroe.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);
    private static final int UPGRADE_THRESHOLD = 4;

    public Shiroe() {
        super(DATA);

        Initialize(0, 0, 2,2);
        SetUpgrade(0, 0, 1);

        SetRetain(true);
        SetUnique(true, true);
        SetShapeshifter();
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster enemy : GameUtilities.GetEnemies(true)) {
            GameActions.Bottom.StackPower(p, new ChokePower(enemy, magicNumber));
        }

        CombatStats.IncreaseSynergyBonus(secondaryValue);

        if (CombatStats.SynergiesThisTurn() >= UPGRADE_THRESHOLD && CombatStats.TryActivateLimited(cardID))
        {
            final float pos_x = (float) Settings.WIDTH / 4f;
            final float pos_y = (float) Settings.HEIGHT / 2f;

            upgrade();

            player.bottledCardUpgradeCheck(this);

            if (GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(pos_x, pos_y));
                GameEffects.TopLevelQueue.ShowCardBriefly(makeStatEquivalentCopy(), pos_x, pos_y);
            }
        }
    }
}