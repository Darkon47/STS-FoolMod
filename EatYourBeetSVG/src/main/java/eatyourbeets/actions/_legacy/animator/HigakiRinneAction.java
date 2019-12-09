package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions._legacy.common.ChooseFromPileAction;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.animator.HigakiRinne;
import eatyourbeets.effects.ShuffleEnemiesEffect;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animator.MarkOfPoisonPower;

import java.util.ArrayList;

public class HigakiRinneAction extends AbstractGameAction
{
    private final HigakiRinne higakiRinne;
    private int roll;

    public HigakiRinneAction(HigakiRinne higakiRinne)
    {
        this.higakiRinne = higakiRinne;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    private boolean tryActivate(int chances)
    {
        roll -= chances;

        return roll <= 0;
    }

    public void update()
    {
        roll = AbstractDungeon.cardRandomRng.random(188);
        
        AbstractPlayer p = AbstractDungeon.player;
        if (tryActivate(3))
        {
            GameActionsHelper_Legacy.AddToBottom(new ChooseFromPileAction(1, false, p.hand, (state, card) -> {}, this, "Choose", false));
        }
        if (tryActivate(6)) // 6
        {
            for (int i = 0; i < 3; i++)
            {
                GameActions.Bottom.GainBlock(2);
            }
        }
        else if (tryActivate(6)) // 12
        {
            for (int i = 0; i < 3; i++)
            {
                GameActionsHelper_Legacy.AddToBottom(new DamageRandomEnemyAction(new DamageInfo(p, 3, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
            }
        }
        else if (tryActivate(6)) // 18
        {
            GameActionsHelper_Legacy.ChannelRandomOrb(true);
        }
        else if (tryActivate(6)) // 24
        {
            GameActions.Bottom.Draw(1);
        }
        else if (tryActivate(8)) // 32
        {
            GameActionsHelper_Legacy.AddToBottom(new RandomCardUpgrade());
        }
        else if (tryActivate(8)) // 40
        {
            GameActions.Bottom.GainIntellect(1);
        }
        else if (tryActivate(6)) // 46
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            GameActionsHelper_Legacy.AddToBottom(new BouncingFlaskAction(m, 2, 2));
        }
        else if (tryActivate(6)) // 52
        {
            GameActions.Bottom.GainEnergy(1);
        }
        else if (tryActivate(6)) // 58
        {
            GameActions.Bottom.GainAgility(1);
        }
        else if (tryActivate(6)) // 64
        {
            GameActions.Bottom.GainForce(1);
        }
        else if (tryActivate(4)) // 68
        {
            GameActions.Bottom.GainArtifact(1);
        }
        else if (tryActivate(6)) // 74
        {
            GameActions.Bottom.GainTemporaryHP(2);
        }
        else if (tryActivate(8)) // 82
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper_Legacy.AddToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
            }
        }
        else if (tryActivate(8)) // 90
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper_Legacy.AddToBottom(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
            }
        }
        else if (tryActivate(8)) // 98
        {
            GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (tryActivate(4)) // 102
        {
            GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInHandAction(new Madness()));
        }
        else if (tryActivate(6)) // 108
        {
            GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInHandAction(new Slimed()));
        }
        else if (tryActivate(3)) // 111
        {
            AbstractCard card = CardLibrary.getRandomColorSpecificCard(higakiRinne.color, AbstractDungeon.cardRandomRng);
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInHandAction(card));
            }
        }
        else if (tryActivate(7)) // 118
        {
            GameActions.Bottom.SFX(JavaUtilities.GetRandomElement(sounds));
        }
        else if (tryActivate(6)) // 124
        {
            GameActionsHelper_Legacy.AddToBottom(new TalkAction(true, "???", 1.0F, 2.0F));
        }
        else if (tryActivate(2)) // 126
        {
            ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());
            String key = JavaUtilities.GetRandomElement(keys);
            AbstractCard card = CardLibrary.cards.get(key).makeCopy();
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInHandAction(card));
            }
        }
        else if (tryActivate(6)) // 132
        {
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActionsHelper_Legacy.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_HEAVY);
            }
        }
        else if (tryActivate(6)) // 138
        {
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActionsHelper_Legacy.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HEAVY);
            }
        }
        else if (tryActivate(6)) // 144
        {
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActionsHelper_Legacy.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.POISON);
            }
        }
        else if (tryActivate(6)) // 150
        {
            GameActions.Bottom.GainBlock(1);
            GameActions.Bottom.GainBlock(1);
            GameActions.Bottom.GainBlock(1);
        }
        else if (tryActivate(6)) // 156
        {
            GameActionsHelper_Legacy.AddToBottom(new IncreaseMaxOrbAction(1));
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }
        else if (tryActivate(4)) // 160
        {
            GameActions.Bottom.GainTemporaryHP(5);
        }
        else if (tryActivate(3)) // 163
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper_Legacy.AddToBottom(new ApplyPowerAction(m, p, new ConstrictedPower(m, p, 3), 3));
            }
        }
        else if (tryActivate(3)) // 166
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper_Legacy.AddToBottom(new ApplyPowerAction(m, p, new BurningPower(m, p, 2), 2));
            }
        }
        else if (tryActivate(3)) // 169
        {
            GameActions.Bottom.GainPlatedArmor(1);
        }
        else if (tryActivate(3)) // 172
        {
            GameActionsHelper_Legacy.Motivate(1);
        }
        else if (tryActivate(3)) // 175
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper_Legacy.AddToBottom(new ApplyPowerAction(m, p, new MarkOfPoisonPower(m, p, 2), 2));
            }
        }
        else if (tryActivate(3)) // 178
        {
            GameActions.Bottom.Draw(3);
        }
        else if (tryActivate(2)) // 180
        {
            GameActionsHelper_Legacy.AddToBottom(new HigakiRinneAction(higakiRinne));
            GameActionsHelper_Legacy.AddToBottom(new HigakiRinneAction(higakiRinne));
            GameActionsHelper_Legacy.AddToBottom(new HigakiRinneAction(higakiRinne));
        }
        else if (tryActivate(5)) // 185
        {
            for (AbstractGameEffect effect : AbstractDungeon.effectList)
            {
                if (effect instanceof ShuffleEnemiesEffect)
                {
                    GameActions.Bottom.StackPower(new EnchantedArmorPower(p, 1));
                    this.isDone = true;
                    return;
                }
            }

            for (AbstractGameEffect effect : AbstractDungeon.effectsQueue)
            {
                if (effect instanceof ShuffleEnemiesEffect)
                {
                    GameActions.Bottom.StackPower(new EnchantedArmorPower(p, 1));
                    this.isDone = true;
                    return;
                }
            }

            AbstractDungeon.effectsQueue.add(new ShuffleEnemiesEffect());
        }

        this.isDone = true;
    }

    public static void PlayRandomSound()
    {
        GameActions.Bottom.SFX(JavaUtilities.GetRandomElement(sounds));
    }

    private static final ArrayList<String> sounds = new ArrayList<>();

    static
    {
        sounds.add("VO_AWAKENEDONE_3");
        sounds.add("VO_GIANTHEAD_1B");
        sounds.add("VO_GREMLINANGRY_1A");
        sounds.add("VO_GREMLINCALM_2A");
        sounds.add("VO_GREMLINFAT_2A");
        sounds.add("VO_GREMLINNOB_1B");
        sounds.add("VO_HEALER_1A");
        sounds.add("VO_MERCENARY_1B");
        sounds.add("VO_MERCHANT_MB");
        sounds.add("VO_SLAVERBLUE_2A");
        sounds.add("THUNDERCLAP");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("NECRONOMICON");
        sounds.add("INTIMIDATE");
    }
}
