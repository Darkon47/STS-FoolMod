package pinacolada.cards.fool.series.DateALive;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardPreview;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Zadkiel;
import pinacolada.utilities.PCLActions;

public class YoshinoHimekawa extends FoolCard {
    public static final PCLCardData DATA = Register(YoshinoHimekawa.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.AoE).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new YoshinoHimekawa(true), false);
                data.AddPreview(new Zadkiel(), false);
            });
    private boolean transformed = false;

    private YoshinoHimekawa(boolean transformed) {
        this();

        SetTransformed(transformed);
    }

    public YoshinoHimekawa() {
        super(DATA);

        Initialize(0, 2, 5, 2);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Star(0,0,1);

        SetEthereal(true);
        SetExhaust(true);
        SetHaste(true);
        SetCostUpgrade(-1);
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb) {
        if (!transformed) {
            super.renderUpgradePreview(sb);
        }
    }

    @Override
    public PCLCardPreview GetCardPreview() {
        if (transformed) {
            return null;
        }

        return super.GetCardPreview();
    }

    @Override
    public void triggerWhenDrawn() {
        if (!transformed) {
            PCLActions.Top.Discard(this, player.hand).ShowEffect(true, true)
                    .AddCallback(() -> {
                        SetTransformed(true);
                        PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), secondaryValue).ShowEffect(true, true);
                    })
                    .SetDuration(0.15f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        
        if (transformed) {
            PCLActions.Bottom.GainBlur(secondaryValue);
            PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), magicNumber).ShowEffect(true, true);
        }
        else {
            PCLActions.Bottom.GainBlur(secondaryValue);
            if (CombatStats.TryActivateLimited(cardID)) {
                PCLActions.Top.MakeCardInDiscardPile(new Zadkiel()).SetUpgrade(upgraded, false);
            }
        }
    }

    private void SetTransformed(boolean value) {
        transformed = value;

        if (transformed) {
            LoadImage("2");
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        } else {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
        }
    }
}